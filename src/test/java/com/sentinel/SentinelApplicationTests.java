package com.sentinel;

import com.sentinel.agent.SentinelAgentService;
import com.sentinel.model.InvestigationReport;
import com.sentinel.model.VerdictType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SentinelApplicationTests {

    @Autowired
    private SentinelAgentService agentService;

    @Test
    void contextLoads() {
        assertNotNull(agentService);
    }

    @Test
    void testPresetInvestigations() {
        InvestigationReport verifiedReport = agentService.investigatePreset("verified", null);
        assertNotNull(verifiedReport);
        assertEquals(VerdictType.VERIFIED, verifiedReport.getVerdict());
        assertEquals(5, verifiedReport.getSteps().size());
        assertTrue(verifiedReport.getConfidenceScore() > 90.0);

        InvestigationReport modifiedReport = agentService.investigatePreset("modified", null);
        assertNotNull(modifiedReport);
        assertEquals(VerdictType.MODIFIED, modifiedReport.getVerdict());
        assertTrue(modifiedReport.getConfidenceScore() > 80.0);

        InvestigationReport unknownReport = agentService.investigatePreset("unknown", null);
        assertNotNull(unknownReport);
        assertEquals(VerdictType.UNKNOWN, unknownReport.getVerdict());
    }
}

