package org.apollo.update;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.FileDescriptor;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.codec.update.OnDemandResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

/**
 * A worker which services 'on-demand' requests.
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest, IndexedFileSystem> {

	/**
	 * The maximum length of a chunk, in bytes.
	 */
	private static final int CHUNK_LENGTH = 500;

	/**
	 * Creates the 'on-demand' request worker.
	 * @param dispatcher The dispatcher.
	 * @param fs The file system.
	 */
	public OnDemandRequestWorker(UpdateDispatcher dispatcher, IndexedFileSystem fs) {
		super(dispatcher, fs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.RequestWorker#nextRequest(org.apollo.update.
	 * UpdateDispatcher)
	 */
	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextOnDemandRequest();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.RequestWorker#service(java.lang.Object,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected void service(IndexedFileSystem fs, Channel channel, OnDemandRequest request) throws IOException {
		final FileDescriptor desc = request.getFileDescriptor();
		final ByteBuffer buf = fs.getFile(desc);
		final int length = buf.remaining();
		for (int chunk = 0; buf.remaining() > 0; chunk++) {
			int chunkSize = buf.remaining();
			if (chunkSize > CHUNK_LENGTH)
				chunkSize = CHUNK_LENGTH;
			final byte[] tmp = new byte[chunkSize];
			buf.get(tmp, 0, tmp.length);
			final ChannelBuffer chunkData = ChannelBuffers.wrappedBuffer(tmp, 0, chunkSize);
			final OnDemandResponse response = new OnDemandResponse(desc, length, chunk, chunkData);
			channel.write(response);
		}
	}
}
