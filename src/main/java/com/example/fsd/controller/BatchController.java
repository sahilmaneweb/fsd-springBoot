package com.example.fsd.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.fsd.entity.Batch;
import com.example.fsd.repository.BatchRepository;
import com.example.fsd.response.ErrorResponse;
import com.example.fsd.response.JwtUtil;
import com.example.fsd.response.ResponseBean;

import io.jsonwebtoken.Claims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BatchController {
    @Autowired
    public JwtUtil jwtUtil;
    @Autowired
    private BatchRepository batchRepo;

    // Create new batch
    @PostMapping(path="/admin/batch", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createBatch(@RequestHeader("Authorization") String token,@RequestBody Batch batch) {
        ResponseBean response = new ResponseBean();

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin"));
            System.out.println(claims);
            Batch existingBatch = batchRepo.findById(batch.getBatchName()).orElse(null);
            if (existingBatch != null) {
                response.setStatus(false);
                response.setMessage("Batch already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            Batch saved = batchRepo.save(batch);
            response.setStatus(true);
            response.setMessage("Batch created");
            response.setData(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get all batches
    @GetMapping(path="/batch", produces = "application/json")
    public ResponseEntity<?> getAllBatches(@RequestHeader("Authorization") String token) {
        ResponseBean response = new ResponseBean();

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Iterable<Batch> batches = batchRepo.findAll();
            response.setStatus(true);
            response.setMessage("Batches retrieved");
            response.setData(batches);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Get particular batch
    @GetMapping(path="/batch/{batchName}", produces = "application/json")
    public ResponseEntity<?> getBatchByName(@RequestHeader("Authorization") String token,@PathVariable String batchName) {
        ResponseBean response = new ResponseBean();

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Batch batch = batchRepo.findById(batchName).orElse(null);
            if (batch == null) {
                response.setStatus(false);
                response.setMessage("Batch not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.setStatus(true);
            response.setMessage("Batch retrieved");
            response.setData(batch);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Update a batch
    @PutMapping(path="/admin/batch/{batchName}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateBatch(@RequestHeader("Authorization") String token,@PathVariable String batchName, @RequestBody Batch batch) {
        ResponseBean response = new ResponseBean();

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin"));
            System.out.println(claims);
            Batch existingBatch = batchRepo.findById(batchName).orElse(null);
            if (existingBatch == null) {
                response.setStatus(false);
                response.setMessage("Batch not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            existingBatch.setVenue(batch.getVenue());;

            Batch updated = batchRepo.save(existingBatch);
            response.setStatus(true);
            response.setMessage("Batch updated");
            response.setData(updated);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    // Delete a batch
    @DeleteMapping(path="/admin/batch/{batchName}", produces = "application/json")
    public ResponseEntity<?> deleteBatch(@RequestHeader("Authorization") String token,@PathVariable String batchName) {
        ResponseBean response = new ResponseBean();

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin"));
            System.out.println(claims);
            Batch existingBatch = batchRepo.findById(batchName).orElse(null);
            if (existingBatch == null) {
                response.setStatus(false);
                response.setMessage("Batch not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            batchRepo.delete(existingBatch);
            response.setStatus(true);
            response.setMessage("Batch with batchName : " + batchName + " deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

    
