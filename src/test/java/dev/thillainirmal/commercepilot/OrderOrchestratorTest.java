package dev.thillainirmal.commercepilot;
import org.junit.jupiter.api.Test;import java.math.BigDecimal;import static org.assertj.core.api.Assertions.assertThat;
class OrderOrchestratorTest{private final OrderOrchestrator o=new OrderOrchestrator();
 @Test void approvesSafeInStockOrder(){var d=o.orchestrate(new OrderOrchestrator.Order("c","HEADSET-X",2,new BigDecimal("100"),"IN",false,"silver"));assertThat(d.status()).isEqualTo("APPROVED");assertThat(d.proposedActions()).hasSize(3);}
 @Test void blocksOutOfStockWithoutWrites(){var d=o.orchestrate(new OrderOrchestrator.Order("c","CAM-4K",1,new BigDecimal("200"),"IN",false,"gold"));assertThat(d.status()).isEqualTo("REJECTED");assertThat(d.safetyBoundary()).contains("plans");}
 @Test void routesRiskyOrderToHuman(){var d=o.orchestrate(new OrderOrchestrator.Order("c","LAPTOP-PRO",2,new BigDecimal("900"),"ZZ",true,"silver"));assertThat(d.status()).isEqualTo("MANUAL_REVIEW");assertThat(d.riskScore()).isGreaterThanOrEqualTo(65);}
 @Test void appliesVolumeDiscount(){var d=o.orchestrate(new OrderOrchestrator.Order("c","HEADSET-X",5,new BigDecimal("100"),"IN",false,"silver"));assertThat(d.total()).isEqualByComparingTo("450.00");}
}
