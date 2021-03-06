package ca.binder.remote.request;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.binder.domain.Match;
import ca.binder.remote.Server;
import ca.binder.remote.binding.MatchBinding;

/**
 * @author Mitchell Hentges
 * @since 11/11/2015
 */
public class GetMatchesRequest implements IServerRequest<List<Match>> {
    @Override
    public Object request(Server server) {
        try {
            Request request = server.request("matches")
                    .get()
                    .build();
            Response response = server.execute(request);
            if (response.code() != 200) {
                return false;
            }

            MatchBinding binding = new MatchBinding();
            List<Match> toReturn = new ArrayList<>();
            JSONArray json = new JSONArray(response.body().string());
            for (int i = 0; i < json.length(); i++) {
                Match match = binding.model(json.getJSONObject(i));
                if (match == null) {
                    continue;
                }
                toReturn.add(match);
            }
            return toReturn;
        } catch (IOException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }
    }
}
