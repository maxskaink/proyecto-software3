package unicauca.coreservice.domain.useCases;

import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

public class ProgramCompetencyAndRAProgramaService implements ProgramCompetencyAndOutcomeInt {

    private final ProgramCompetencyAndOutcomeRepositoryOutInt repository;

    public ProgramCompetencyAndRAProgramaService(ProgramCompetencyAndOutcomeRepositoryOutInt repository) {
        this.repository = repository;
    }

    @Override
    public ProgramCompetency addProgramCompetency(ProgramCompetency newProgramCompetency) throws Exception {
        OptionalWrapper<ProgramCompetency> response = repository.add(newProgramCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public List<ProgramCompetency> getProgramCompetencies() {
        return repository.listAll();
    }

    @Override
    public ProgramCompetency getCompetencyById(Integer id) throws Exception {
        if(null == id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<ProgramCompetency> response = repository.getCompetencyById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramCompetency updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null== newProgramCompetency)
            throw new InvalidValue("Instance de competencia es invalida, no puede ser nula");

        OptionalWrapper<ProgramCompetency> response = repository.updateProgramCompetency(id, newProgramCompetency);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramCompetency deleteProgramCompetency(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<ProgramCompetency> response = repository.remove(id);

        ProgramCompetency compDeleted = response.getValue()
                .orElseThrow(response::getException);
        return compDeleted;
    }

    @Override
    public List<ProgramOutcome> getProgramOutcomes() {
        return repository.getProgramOutcome();
    }

    @Override
    public ProgramOutcome getProgramOutcomesById(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<ProgramOutcome> response = repository.getProgramOutcomeById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramOutcome updateProgramOutcomes(Integer id, ProgramOutcome newProgramOutcome) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null== newProgramOutcome)
            throw new InvalidValue("Instance de RA es invalida, no puede ser nula");
        OptionalWrapper<ProgramOutcome> response = repository.updateProgramOutcome(id, newProgramOutcome);
        return response.getValue()
                .orElseThrow(response::getException);
    }

}
