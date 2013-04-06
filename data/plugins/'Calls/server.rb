on :world, :server, :eval do |request|
  eval_d = request.data.get "eval"
  if eval_d != nil
    eval_response = eval eval_d
    WorldResponse.new GSON.to_json(eval_response)
  end
end