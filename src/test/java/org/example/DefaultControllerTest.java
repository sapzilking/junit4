package org.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultControllerTest {
    private DefaultController controller;
    private Request request;
    private RequestHandler handler;

    @Before
    public void initialize() {
        controller = new DefaultController();
        request = new SampleRequest();
        handler = new SampleHandler();
        controller.addHandler(request, handler);
    }

    private class SampleRequest implements Request {
        private static final String DEFAULT_NAME = "Test";
        private String name;

        public SampleRequest(String name) {
            this.name = name;
        }

        public SampleRequest() {
            this(DEFAULT_NAME);
        }

        public String getName() {
            return this.name;
        }
    }

    private class SampleHandler implements RequestHandler {
        @Override
        public Response process(Request request) throws Exception {
            return new SampleResponse();
        }
    }

    private class SampleExceptionHandler implements RequestHandler {

        @Override
        public Response process(Request request) throws Exception {
            throw new Exception("error processing request");
        }
    }


    private class SampleResponse implements Response {
        private static final String NAME = "Test";

        public String getName() {
            return NAME;
        }

        @Override
        public boolean equals(Object object) {
            boolean result = false;
            if (object instanceof SampleRequest) {
                result = ((SampleResponse) object).getName().equals(getName());
            }
            return result;
        }

        @Override
        public int hashCode() {
            return NAME.hashCode();
        }
    }

    @Test
    public void testAddHandler() {
        RequestHandler handler2 = controller.getHandler(request);
        assertSame("Handler we set in controller should be the same handler we get", handler2, handler);
    }

    @Test
    public void testProcessRequest() {
        Response response = controller.processRequest(request);
        assertNotNull("Must not return a null response", response);
        assertEquals("Response should be of type SampleResponse", SampleResponse.class, response.getClass());
        assertEquals(new SampleResponse(), response);
    }

    @Test
    public void testProcessRequestAnswersErrorResponse() {
        SampleRequest request = new SampleRequest("testError");
        SampleExceptionHandler handler = new SampleExceptionHandler();
        controller.addHandler(request, handler);
        Response response = controller.processRequest(request);
        assertNotNull("Must not return a null response", response);
        assertEquals(ErrorResponse.class, response.getClass());
    }


}