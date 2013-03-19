package org.apollo.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apollo.game.model.Character;

/**
 * A {@link CharacterRepository} is a repository of {@link Character}s that are
 * currently active in the game world.
 * @param <T> The type of character.
 * @author Graham
 */
public final class CharacterRepository<T extends Character> implements Iterable<T> {

	/**
	 * The {@link Iterator} implementation for the {@link CharacterRepository}
	 * class.
	 * @author Graham
	 */
	private final class CharacterRepositoryIterator implements Iterator<T> {

		/**
		 * The previous index of this iterator.
		 */
		private int previousIndex = -1;

		/**
		 * The current index of this iterator.
		 */
		private int index = 0;

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			for (int i = index; i < characters.length; i++)
				if (characters[i] != null) {
					index = i;
					return true;
				}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			T character = null;
			for (int i = index; i < characters.length; i++)
				if (characters[i] != null) {
					character = (T) characters[i];
					index = i;
					break;
				}
			if (character == null)
				throw new NoSuchElementException();
			previousIndex = index;
			index++;
			return character;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void remove() {
			if (previousIndex == -1)
				throw new IllegalStateException();
			CharacterRepository.this.remove((T) characters[previousIndex]);
			previousIndex = -1;
		}
	}

	/**
	 * The array of characters in this repository.
	 */
	private final Character[] characters;

	/**
	 * The current size of this repository.
	 */
	private int size = 0;

	/**
	 * The position of the next free index.
	 */
	private int pointer = 0;

	/**
	 * Creates a new character repository with the specified capacity.
	 * @param capacity The maximum number of characters that can be present in
	 * the repository.
	 */
	public CharacterRepository(int capacity) {
		this.characters = new Character[capacity];
	}

	/**
	 * Adds a character to the repository.
	 * @param character The character to add.
	 * @return {@code true} if the character was added, {@code false} if the
	 * size has reached the capacity of this
	 * repository.
	 */
	public boolean add(T character) {
		if (size == characters.length)
			return false;
		int index = -1;
		for (int i = pointer; i < characters.length; i++)
			if (characters[i] == null) {
				index = i;
				break;
			}
		if (index == -1)
			for (int i = 0; i < pointer; i++)
				if (characters[i] == null) {
					index = i;
					break;
				}
		if (index == -1)
			return false; // shouldn't happen, but just in case
		characters[index] = character;
		character.setIndex(index + 1);
		if (index == characters.length - 1)
			pointer = 0;
		else
			pointer = index;
		size++;
		return true;
	}

	/**
	 * Gets the capacity of this repository.
	 * @return The maximum size of this repository.
	 */
	public int capacity() {
		return characters.length;
	}

	/**
	 * Checks if the character is in the list.
	 * @param character The character to look for.
	 * @return True if listed, false if otherwise.
	 */
	public boolean contains(Character character) {
		if (character == null)
			return false;
		for (final Character entity : characters)
			if (character.equals(entity))
				return true;
		return false;
	}

	/**
	 * Gets a index.
	 * @param index The index.
	 * @return T the class.
	 */
	@SuppressWarnings("unchecked")
	public T forIndex(int index) {
		return (T) characters[index - 1];
	}

	/**
	 * Get a index.
	 * @param index the index
	 * @return {@link Character}
	 */
	public Character get(int index) {
		Character character = null;
		if (characters[index] != null)
			character = characters[index];
		if (character == null)
			throw new NoSuchElementException();
		return character;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new CharacterRepositoryIterator();
	}

	/**
	 * Removes a character from the repository.
	 * @param character The character to remove.
	 * @return {@code true} if the character was removed, {@code false} if it
	 * was not (e.g. if it was never added or has
	 * been removed already).
	 */
	public boolean remove(T character) {
		final int index = character.getIndex() - 1;
		if (index < 0 || index >= characters.length)
			return false;
		if (characters[index] == character) {
			characters[index] = null;
			character.setIndex(-1);
			size--;
			return true;
		}
		else
			return false;
	}

	/**
	 * Gets the size of this repository.
	 * @return The number of characters in this repository.
	 */
	public int size() {
		return size;
	}
}
