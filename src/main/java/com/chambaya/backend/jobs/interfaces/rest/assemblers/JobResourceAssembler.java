package com.chambaya.backend.jobs.interfaces.rest.assemblers;

import com.chambaya.backend.jobs.application.commads.CreateJobCommand;
import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.Location;
import com.chambaya.backend.jobs.interfaces.rest.resources.CreateJobResource;
import com.chambaya.backend.jobs.interfaces.rest.resources.JobResource;
import com.chambaya.backend.jobs.interfaces.rest.resources.LocationResource;

public class JobResourceAssembler {

    private JobResourceAssembler() {}

    public static CreateJobCommand toCreatJobCommand(CreateJobResource resource){
        return new CreateJobCommand(
                resource.contractorId(),
                resource.title(),
                resource.description(),
                resource.category(),
                resource.requiredSkills(),
                resource.paymentAmount(),
                resource.latitude(),
                resource.longitude(),
                resource.address(),
                resource.district(),
                resource.scheduledStart(),
                resource.scheduledEnd()
        );
    }

    public static JobResource toResource(Job job){
        return new JobResource(
                job.getId(),
                job.getContractorId(),
                job.getTitle(),
                job.getDescription(),
                job.getCategory(),
                job.getRequiredSkills(),
                job.getPaymentAmount(),
                toLocationResource(job.getLocation()),
                job.getScheduledStart(),
                job.getScheduledEnd(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    private static LocationResource toLocationResource(Location location){
        if (location == null) {
            return null;
        }
        return new LocationResource(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getDistrict()
        );
    }
}
