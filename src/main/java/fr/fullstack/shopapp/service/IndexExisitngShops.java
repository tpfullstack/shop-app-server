package fr.fullstack.shopapp.service;


import fr.fullstack.shopapp.model.Shop;
import fr.fullstack.shopapp.model.SyncStatus;
import fr.fullstack.shopapp.repository.elastic.ShopElasticRepository;
import fr.fullstack.shopapp.repository.jpa.ShopRepository;
import fr.fullstack.shopapp.repository.jpa.SyncStatusRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndexExisitngShops {

    private static final Logger log = LoggerFactory.getLogger(IndexExisitngShops.class);
    @PersistenceContext
    EntityManager em;

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
            IndexExisitngShops.log.info("Synchronization has already been completed.");
            return;
        }

        Page<Shop> shops = shopService.getShopList(Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(), Optional.empty(), Pageable.unpaged());
        shops.forEach(shop -> {
            try {
                shopService.syncExistingShopsToElasticsearch(shop);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SyncStatus status = new SyncStatus();
        status.setSyncCompleted(true);
        syncStatusRepository.save(status);

        IndexExisitngShops.log.info("Successfully synced " + shops + " shops to Elasticsearch.");
    }
}
