package acetil.magicalreactors.common.utils;

import acetil.magicalreactors.common.MagicalReactors;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    public static List<Path> getPaths (URI uri) {
        List<Path> paths = new ArrayList<>();
        if (uri == null) {
            return paths;
        }
        try {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            paths = Files.walk(Paths.get(uri))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            MagicalReactors.LOGGER.log(Level.ERROR,"Error walking path " + uri.getPath());
            MagicalReactors.LOGGER.log(Level.ERROR, e);
        }
        return paths;
    }
    public static URI getURI (String path) {
        URL url = FileUtils.class.getClassLoader().getResource(path);
        if (url == null) {
            MagicalReactors.LOGGER.log(Level.ERROR, "Invalid path: " + path);
            return null;
        }
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            MagicalReactors.LOGGER.log(Level.ERROR, "Invalid path: " + path);
            MagicalReactors.LOGGER.log(Level.ERROR, e);
        }
        return uri;
    }
    public static void closeFileSystem (URI uri) {
        if (uri == null) {
            return;
        }
        try {
            FileSystems.getFileSystem(uri).close();
        } catch (IOException e) {
            MagicalReactors.LOGGER.log(Level.ERROR, "Error closing file system at path " + uri.getPath());
            MagicalReactors.LOGGER.log(Level.ERROR, e);
        }
    }
}
