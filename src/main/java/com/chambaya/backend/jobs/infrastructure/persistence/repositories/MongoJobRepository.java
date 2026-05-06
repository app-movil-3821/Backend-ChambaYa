package com.chambaya.backend.jobs.infrastructure.persistence.repositories;

import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.JobStatus;
import com.chambaya.backend.jobs.domain.repositories.JobRepository;
import com.chambaya.backend.jobs.infrastructure.persistence.documents.JobDocument;
import com.chambaya.backend.jobs.infrastructure.persistence.mappers.JobMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoJobRepository implements JobRepository {

    private final SpringDataJobRepository springDataJobRepository;
    public MongoJobRepository(SpringDataJobRepository springDataJobRepository) {
        this.springDataJobRepository = springDataJobRepository;
    }
    @Override
    public Job save(Job job){
        JobDocument document = JobMapper.toDocument(job);
        JobDocument savedDocument = springDataJobRepository.save(document);
        return JobMapper.toDomain(savedDocument);
    }
    @Override
    public Optional<Job> findById(String id){
        return springDataJobRepository.findById(id)
                .map(JobMapper::toDomain);
    }
    @Override
    public List<Job> findAll(){
        return springDataJobRepository.findAll()
                .stream()
                .map(JobMapper::toDomain)
                .toList();
    }
    @Override
    public List<Job> findByContractorId(String contractorId){
        return springDataJobRepository.findByContractorId(contractorId)
                .stream()
                .map(JobMapper::toDomain)
                .toList();
    }
    @Override
    public List<Job> findByStatus(JobStatus status){
        return springDataJobRepository.findByStatus(status)
                .stream()
                .map(JobMapper::toDomain)
                .toList();
    }
    @Override
    public void deleteById(String id){
        springDataJobRepository.deleteById(id);
    }
}
