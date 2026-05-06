package com.chambaya.backend.jobs.infrastructure.persistence.mappers;

import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.Location;
import com.chambaya.backend.jobs.infrastructure.persistence.documents.JobDocument;
import com.chambaya.backend.jobs.infrastructure.persistence.documents.LocationDocument;

public class JobMapper {

    private JobMapper() {}

    public static JobDocument toDocument(Job job){
        if (job == null) {
            return null;
        }

        return new JobDocument(
                job.getId(),
                job.getContractorId(),
                job.getTitle(),
                job.getDescription(),
                job.getCategory(),
                job.getRequiredSkills(),
                job.getPaymentAmount(),
                toLocationDocument(job.getLocation()),
                job.getScheduledStart(),
                job.getScheduledEnd(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getUpdatedAt()

        );
    }

    public static Job toDomain(JobDocument document){
        if (document == null) {
            return null;
        }
        return new Job(
                document.getId(),
                document.getContractorId(),
                document.getTitle(),
                document.getDescription(),
                document.getCategory(),
                document.getRequiredSkills(),
                document.getPaymentAmount(),
                toLocation(document.getLocation()),
                document.getScheduledStart(),
                document.getScheduledEnd(),
                document.getStatus(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }

    private static LocationDocument toLocationDocument(Location location){
        if (location == null) {
            return null;
        }
        return new LocationDocument(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getAddress()
        );
    }

    private static Location toLocation(LocationDocument document){
        if (document == null) {
            return null;
        }
        return new Location(
                document.getLatitude(),
                document.getLongitude(),
                document.getAddress(),
                document.getDistrict()
        );
    }
}
