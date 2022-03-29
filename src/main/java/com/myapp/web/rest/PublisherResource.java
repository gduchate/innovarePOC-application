package com.myapp.web.rest;

import com.myapp.domain.Publisher;
import com.myapp.repository.PublisherRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.Publisher}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PublisherResource {

    private final Logger log = LoggerFactory.getLogger(PublisherResource.class);

    private static final String ENTITY_NAME = "myApp1Publisher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublisherRepository publisherRepository;

    public PublisherResource(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * {@code POST  /publishers} : Create a new publisher.
     *
     * @param publisher the publisher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publisher, or with status {@code 400 (Bad Request)} if the publisher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/publishers")
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) throws URISyntaxException {
        log.debug("REST request to save Publisher : {}", publisher);
        if (publisher.getId() != null) {
            throw new BadRequestAlertException("A new publisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity
            .created(new URI("/api/publishers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /publishers/:id} : Updates an existing publisher.
     *
     * @param id the id of the publisher to save.
     * @param publisher the publisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publisher,
     * or with status {@code 400 (Bad Request)} if the publisher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/publishers/{id}")
    public ResponseEntity<Publisher> updatePublisher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Publisher publisher
    ) throws URISyntaxException {
        log.debug("REST request to update Publisher : {}, {}", id, publisher);
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publisher.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /publishers/:id} : Partial updates given fields of an existing publisher, field will ignore if it is null
     *
     * @param id the id of the publisher to save.
     * @param publisher the publisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publisher,
     * or with status {@code 400 (Bad Request)} if the publisher is not valid,
     * or with status {@code 404 (Not Found)} if the publisher is not found,
     * or with status {@code 500 (Internal Server Error)} if the publisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/publishers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Publisher> partialUpdatePublisher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Publisher publisher
    ) throws URISyntaxException {
        log.debug("REST request to partial update Publisher partially : {}, {}", id, publisher);
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Publisher> result = publisherRepository
            .findById(publisher.getId())
            .map(existingPublisher -> {
                if (publisher.getName() != null) {
                    existingPublisher.setName(publisher.getName());
                }
                if (publisher.getAddress() != null) {
                    existingPublisher.setAddress(publisher.getAddress());
                }

                return existingPublisher;
            })
            .map(publisherRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publisher.getId().toString())
        );
    }

    /**
     * {@code GET  /publishers} : get all the publishers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishers in body.
     */
    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        log.debug("REST request to get all Publishers");
        return publisherRepository.findAll();
    }

    /**
     * {@code GET  /publishers/:id} : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publisher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable Long id) {
        log.debug("REST request to get Publisher : {}", id);
        Optional<Publisher> publisher = publisherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(publisher);
    }

    /**
     * {@code DELETE  /publishers/:id} : delete the "id" publisher.
     *
     * @param id the id of the publisher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        log.debug("REST request to delete Publisher : {}", id);
        publisherRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
