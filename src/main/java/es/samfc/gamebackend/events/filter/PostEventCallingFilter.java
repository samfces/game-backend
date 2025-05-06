package es.samfc.gamebackend.events.filter;

import es.samfc.gamebackend.events.producer.EventProducer;
import es.samfc.gamebackend.events.types.RestEvent;
import es.samfc.gamebackend.events.types.RestRequestType;
import es.samfc.gamebackend.events.wrapper.HttpServletRequestWrapper;
import es.samfc.gamebackend.events.wrapper.HttpServletResponseWrapper;
import es.samfc.gamebackend.events.wrapper.snapshots.RequestSnapshot;
import es.samfc.gamebackend.events.wrapper.snapshots.ResponseSnapshot;
import es.samfc.gamebackend.utils.events.RequestSnapshotUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PostEventCallingFilter extends OncePerRequestFilter {

    private final EventProducer eventProducer;

    public PostEventCallingFilter(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();
        RestRequestType restRequestType = RestRequestType.getByMethodAndPath(method, path);
        if (restRequestType == null) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        RequestSnapshot requestSnapshot = RequestSnapshotUtil.from(requestWrapper);
        ResponseSnapshot responseSnapshot = responseWrapper.toSnapshot();

        eventProducer.callEvent(new RestEvent.Builder<>()
                .eventType(restRequestType)
                .requestData(requestSnapshot)
                .responseData(responseSnapshot)
                .build());

        responseWrapper.copyBodyToResponse();

    }
}
