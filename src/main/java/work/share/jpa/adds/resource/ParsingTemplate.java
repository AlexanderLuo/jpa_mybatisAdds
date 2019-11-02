package work.share.jpa.adds.resource;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ParsingTemplate {


    void parse(Resource resource) throws IOException, ClassNotFoundException;


}
