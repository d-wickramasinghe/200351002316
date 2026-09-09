package com.example.services;

import org.springframework.stereotype.Service;

import com.example.dto.NominationRequestDTO;
import com.example.dto.NominationResponseDTO;
import com.example.entity.Nomination;
import com.example.exception.DuplicateNominationException;
import com.example.repository.NominationRepository;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;

    public NominationService(NominationRepository nominationRepository) {
        this.nominationRepository = nominationRepository;
    }

    public NominationResponseDTO createNomination(
            NominationRequestDTO request) {

        // Check for duplicate nomination
        if (nominationRepository
                .findByOfficerNameAndTrainingTitleAndDepartmentName(
                        request.getOfficerName(),
                        request.getTrainingTitle(),
                        request.getDepartmentName())
                .isPresent()) {

            throw new DuplicateNominationException(
                    "Officer is already nominated for this training programme by this department"
            );
        }

        // Create nomination
        Nomination nomination = new Nomination();

        nomination.setOfficerName(request.getOfficerName());
        nomination.setTrainingTitle(request.getTrainingTitle());
        nomination.setDepartmentName(request.getDepartmentName());

        // Save nomination
        Nomination saved =
                nominationRepository.save(nomination);

        // Return response
        return mapToResponse(saved);
    }

    private NominationResponseDTO mapToResponse(
            Nomination nomination) {

        NominationResponseDTO response =
                new NominationResponseDTO();

        response.setId(nomination.getId());
        response.setOfficerName(nomination.getOfficerName());
        response.setTrainingTitle(nomination.getTrainingTitle());
        response.setDepartmentName(nomination.getDepartmentName());

        return response;
    }
}