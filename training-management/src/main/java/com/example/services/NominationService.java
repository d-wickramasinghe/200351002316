package com.example.services;

import org.springframework.stereotype.Service;


import com.example.dto.NominationRequestDTO;
import com.example.dto.NominationResponseDTO;
import com.example.entity.Nomination;
import com.example.repository.NominationRepository;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;

    public NominationService(NominationRepository nominationRepository) {
        this.nominationRepository = nominationRepository;
    }

    public NominationResponseDTO createNomination(
            NominationRequestDTO request) {

        // duplicate check
        if (nominationRepository
                .findByOfficerNameAndTrainingTitle(
                        request.getOfficerName(),
                        request.getTrainingTitle())
                .isPresent()) {

            throw new RuntimeException(
                    "Officer is already nominated for this training programme");
        }

        Nomination nomination = new Nomination();

        nomination.setOfficerName(request.getOfficerName());
        nomination.setTrainingTitle(request.getTrainingTitle());
        nomination.setDepartmentName(request.getDepartmentName());

        Nomination saved =
                nominationRepository.save(nomination);

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