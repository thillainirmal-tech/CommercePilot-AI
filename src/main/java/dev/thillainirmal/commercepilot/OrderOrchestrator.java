package dev.thillainirmal.commercepilot;
import org.springframework.stereotype.Service;import java.math.BigDecimal;import java.math.RoundingMode;import java.util.*;
@Service public class OrderOrchestrator{
 public Decision orchestrate(Order order){
  var trace=new ArrayList<Step>();var actions=new ArrayList<String>();
  int available=inventory().getOrDefault(order.sku(),0);boolean stock=available>=order.quantity();
  trace.add(new Step("inventory-agent",stock?"PASS":"BLOCK",stock?available+" units available":"insufficient stock"));
  int risk=risk(order);boolean safe=risk<65;trace.add(new Step("risk-agent",safe?"PASS":"REVIEW","risk score "+risk+"/100"));
  BigDecimal subtotal=order.unitPrice().multiply(BigDecimal.valueOf(order.quantity()));BigDecimal discount=discount(order,subtotal);BigDecimal total=subtotal.subtract(discount).setScale(2,RoundingMode.HALF_UP);
  trace.add(new Step("pricing-agent","PASS","discount "+discount+", total "+total));
  String status;if(!stock){status="REJECTED";actions.add("Suggest restock or alternate SKU");}else if(!safe){status="MANUAL_REVIEW";actions.add("Hold payment authorization");actions.add("Request human fraud review");}else{status="APPROVED";actions.add("Reserve "+order.quantity()+" unit(s)");actions.add("Authorize payment "+total);actions.add("Create tracked shipment");}
  trace.add(new Step("fulfillment-agent",status,status.equals("APPROVED")?"reservation and shipment planned":"no irreversible action executed"));
  return new Decision(UUID.nameUUIDFromBytes((order.customerId()+order.sku()+order.quantity()).getBytes()).toString(),status,total,risk,List.copyOf(actions),List.copyOf(trace),"All actions are plans; external writes require an approved adapter.");
 }
 private int risk(Order o){int score=0;if(o.unitPrice().multiply(BigDecimal.valueOf(o.quantity())).compareTo(new BigDecimal("1500"))>0)score+=45;if(o.newCustomer())score+=25;if(!Set.of("IN","US","GB","SG","DE").contains(o.country()))score+=35;return Math.min(score,100);}
 private BigDecimal discount(Order o,BigDecimal subtotal){if(o.quantity()>=5)return subtotal.multiply(new BigDecimal("0.10")).setScale(2,RoundingMode.HALF_UP);if(o.loyaltyTier().equalsIgnoreCase("gold"))return subtotal.multiply(new BigDecimal("0.05")).setScale(2,RoundingMode.HALF_UP);return BigDecimal.ZERO.setScale(2);}
 private Map<String,Integer> inventory(){return Map.of("LAPTOP-PRO",7,"HEADSET-X",24,"CAM-4K",0,"DOCK-USB",12);}
 public record Order(String customerId,String sku,int quantity,BigDecimal unitPrice,String country,boolean newCustomer,String loyaltyTier){}
 public record Step(String agent,String outcome,String evidence){}
 public record Decision(String orderId,String status,BigDecimal total,int riskScore,List<String> proposedActions,List<Step> trace,String safetyBoundary){}
}

