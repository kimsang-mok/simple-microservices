package com.kimsang.microservices.composite.product.services;

import com.kimsang.api.composite.product.*;
import com.kimsang.api.core.product.Product;
import com.kimsang.api.core.recommendation.Recommendation;
import com.kimsang.api.core.review.Review;
import com.kimsang.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
  private final ServiceUtil serviceUtil;
  private ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public ProductAggregate getProduct(int productId) {
    Product product = integration.getProduct(productId);
    List<Recommendation> recommendations = integration.getRecommendations(productId);
    List<Review> reviews = integration.getReviews(productId);

    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    List<RecommendationSummary> recommendationSummaries =
        (recommendations == null) ? null : recommendations.stream().map(r -> new RecommendationSummary(r.getRecommendationId(),
            r.getAuthor(), r.getRate())).toList();

    List<ReviewSummary> reviewSummarySummaries = (reviews == null) ? null :
        reviews.stream().map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject())).toList();

    // Create info regarding teh involved microservices' addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && !reviews.isEmpty()) ? reviews.getFirst().getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && !recommendations.isEmpty()) ? recommendations.getFirst().getServiceAddress() :
        "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);
    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummarySummaries, serviceAddresses);
  }
}
