package dev.thillainirmal.commercepilot;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.web.bind.annotation.*;import java.math.BigDecimal;
@RestController @RequestMapping("/api/orders") public class CommerceController{
 private final OrderOrchestrator orchestrator;public CommerceController(OrderOrchestrator o){orchestrator=o;}
 @PostMapping("/plan") public OrderOrchestrator.Decision plan(@Valid @RequestBody OrderRequest r){return orchestrator.orchestrate(new OrderOrchestrator.Order(r.customerId(),r.sku(),r.quantity(),r.unitPrice(),r.country(),r.newCustomer(),r.loyaltyTier()));}
 @GetMapping("/demo") public OrderOrchestrator.Decision demo(){return orchestrator.orchestrate(new OrderOrchestrator.Order("portfolio-user","LAPTOP-PRO",2,new BigDecimal("899.00"),"IN",false,"gold"));}
 public record OrderRequest(@NotBlank String customerId,@NotBlank String sku,@Min(1) int quantity,@DecimalMin("0.01") BigDecimal unitPrice,@NotBlank String country,boolean newCustomer,@NotBlank String loyaltyTier){}
}

