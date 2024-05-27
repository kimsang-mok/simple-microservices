package com.kimsang.microservices.composite.product.services;

import com.kimsang.api.core.product.ProductService;
import com.kimsang.api.core.recommendation.RecommendationService;
import com.kimsang.api.core.review.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
  private final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

  private final RestTemplate restTemplate;

  @Autowired
  public ProductCompositeIntegration(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
}

