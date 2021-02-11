package uk.gov.crowncommercial.dsd.api.auth.logic;

import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.camel.support.DefaultHeaderFilterStrategy;
import org.springframework.stereotype.Component;

/**
 * Filter (remove) all outbound headers except those specified to {@link #setOutFilter(Set)}
 *
 * This will ensure no client-provided query params or Camel exchange headers are sent to the
 * external API
 */
@Component
public class SpreeApiHeaderFilter extends DefaultHeaderFilterStrategy {

  @PostConstruct
  protected void init() {
    setFilterOnMatch(false);
    setOutFilter(Set.of("accept", "accept-encoding", "cache-control", "authorization"));
  }

}
