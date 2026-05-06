package com.chambaya.backend.enrollments.infrastructure.persistence.repositories;

import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import com.chambaya.backend.enrollments.domain.repositories.EnrollmentRepository;
import com.chambaya.backend.enrollments.infrastructure.persistence.documents.EnrollmentDocument;
import com.chambaya.backend.enrollments.infrastructure.persistence.mappers.EnrollmentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoEnrollmentRepository implements EnrollmentRepository {
    private final SpringDataEnrollmentRepository springDataEnrollmentRepository;
    public MongoEnrollmentRepository(SpringDataEnrollmentRepository springDataEnrollmentRepository) {
        this.springDataEnrollmentRepository = springDataEnrollmentRepository;
    }

    @Override
    public Enrollment save(Enrollment enrollment){
        EnrollmentDocument document = EnrollmentMapper.toDocument(enrollment);
        EnrollmentDocument savaDocument = springDataEnrollmentRepository.save(document);
        return EnrollmentMapper.toDomain(savaDocument);
    }
    @Override
    public Optional<Enrollment> findById(String id){
        return springDataEnrollmentRepository.findById(id)
                .map(EnrollmentMapper::toDomain);
    }
    @Override
    public List<Enrollment> findAll(){
        return springDataEnrollmentRepository.findAll()
                .stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }
    @Override
    public List<Enrollment> findByJobId(String jobId){
        return springDataEnrollmentRepository.findByJobId(jobId)
                .stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }
    @Override
    public List<Enrollment> findByWorkerId(String workerId) {
        return springDataEnrollmentRepository.findByWorkerId(workerId)
                .stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }
    @Override
    public List<Enrollment> findByContractorId(String contractorId){
        return springDataEnrollmentRepository.findByContractorId(contractorId)
                .stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Enrollment> findByStatus(EnrollmentStatus status){
        return springDataEnrollmentRepository.findByStatus(status)
                .stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByJobIdAndWorkerId(String jobId, String workerId){
        return springDataEnrollmentRepository.existsByJobIdAndWorkerId(jobId, workerId);
    }
    @Override
    public void deleteById(String id){
        springDataEnrollmentRepository.deleteById(id);
    }

}
