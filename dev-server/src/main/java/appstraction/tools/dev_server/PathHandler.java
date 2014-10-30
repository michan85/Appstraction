package appstraction.tools.dev_server;
/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




/**
 * Handler that dispatches to a given handler based of a prefix match of the path.
 * <p/>
 * This only matches a single level of a request, e.g if you have a request that takes the form:
 * <p/>
 * /foo/bar
 * <p/>
 *
 * @author Stuart Douglas
 */
public class PathHandler implements RequestHandler {

    private final PathMatcher<RequestHandler> pathMatcher = new PathMatcher<RequestHandler>();

    public PathHandler(final RequestHandler defaultHandler) {
        pathMatcher.addPrefixPath("/", defaultHandler);
    }

    public PathHandler() {
    }
    

    public void handleRequest(RequestContext context) throws Exception {
        final PathMatcher.PathMatch<RequestHandler> match = pathMatcher.match(context.getRelativePath());
        if(match.getValue() == null) {
            //ResponseCodeHandler.HANDLE_404.handleRequest(exchange);
        	throw new Exception("Failed to match path");
        }
        context.setRelativePath(match.getRemaining());
        context.setResolvedPath(context.getRequestPath().substring(0, context.getRequestPath().length() - match.getRemaining().length()));
        match.getValue().handleRequest(context);
    }

 
    /**
     * Adds a path prefix and a handler for that path. If the path does not start
     * with a / then one will be prepended.
     * <p/>
     * The match is done on a prefix bases, so registering /foo will also match /bar. Exact
     * path matches are taken into account first.
     * <p/>
     * If / is specified as the path then it will replace the default handler.
     *
     * @param path    The path
     * @param handler The handler
     */
    public synchronized PathHandler addPrefixPath(final String path, final RequestHandler handler) {
        pathMatcher.addPrefixPath(path, handler);
        return this;
    }


    public synchronized PathHandler addExactPath(final String path, final RequestHandler handler) {
        pathMatcher.addExactPath(path, handler);
        return this;
    }

 
    public synchronized PathHandler removePrefixPath(final String path) {
        pathMatcher.removePrefixPath(path);
        return this;
    }

    public synchronized PathHandler removeExactPath(final String path) {
        pathMatcher.removeExactPath(path);
        return this;
    }

    public synchronized PathHandler clearPaths() {
        pathMatcher.clearPaths();
        return this;
    }
}
