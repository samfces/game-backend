package es.samfc.gamebackend.utils.events;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class RequestSnapshotUtilTest {

    @Test
    @DisplayName("Check RequestSnapshot from null request")
    void fromNullRequest() {
        HttpServletRequest request = null;
        assertThat(RequestSnapshotUtil.from(request)).isNull();
    }


    @Test
    @DisplayName("Check RequestSnapshot from request")
    void fromRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThat(RequestSnapshotUtil.from(request)).isNotNull();
    }


    @Test
    @DisplayName("Check RequestSnapshot from wrapper")
    void fromWrapper() {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(new MockHttpServletRequest());
        assertThat(RequestSnapshotUtil.from(wrapper)).isNotNull();
    }

}