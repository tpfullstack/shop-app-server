package fr.fullstack.shopapp.service;


import fr.fullstack.shopapp.model.Shop;
import fr.fullstack.shopapp.model.SyncStatus;
import fr.fullstack.shopapp.repository.elastic.ShopElasticRepository;
import fr.fullstack.shopapp.repository.jpa.ShopRepository;
import fr.fullstack.shopapp.repository.jpa.SyncStatusRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexExisitngShops {

    private final ShopService shopService;
    private final ShopElasticRepository shopElasticRepository;
    private final ShopRepository shopRepository;
    private final SyncStatusRepository syncStatusRepository;

    public IndexExisitngShops(ShopService shopService, ShopElasticRepository shopElasticRepository, ShopRepository shopRepository, SyncStatusRepository syncStatusRepository) {
        this.shopService = shopService;
        this.shopElasticRepository = shopElasticRepository;
        this.shopRepository = shopRepository;
        this.syncStatusRepository = syncStatusRepository;
    }

    @PostConstruct
    public void syncDatabaseToElasticsearch() {
        // Check if synchronization has already been completed
        if (syncStatusRepository.existsBySyncCompletedTrue()) {
            System.out.println("Synchronization has already been completed.");
            return;
        }
        List<Shop> products = shopRepository.findAll();
        shopElasticRepository.saveAll(products);

        SyncStatus status = new SyncStatus();
        status.setSyncCompleted(true);
        syncStatusRepository.save(status);

        System.out.println("Successfully synced " + products.size() + " products to Elasticsearch.");
    }
}
