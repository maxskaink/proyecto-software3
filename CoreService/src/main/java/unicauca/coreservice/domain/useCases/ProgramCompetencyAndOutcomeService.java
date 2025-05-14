package unicauca.coreservice.domain.useCases;

import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;

import java.util.List;

public class ProgramCompetencyAndOutcomeService implements ProgramCompetencyAndOutcomeInt {

    private final ProgramCompetencyAndOutcomeRepositoryOutInt repository;

    public ProgramCompetencyAndOutcomeService(ProgramCompetencyAndOutcomeRepositoryOutInt repository) {
        this.repository = repository;
    }

    @Override
    public ProgramCompetency add(ProgramCompetency newProgramCompetency) throws Exception {
        OptionalWrapper<ProgramCompetency> response = repository.add(newProgramCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public List<ProgramCompetency> listAllProgramCompetencies() {
        return repository.listAll();
    }

    @Override
    public ProgramCompetency getProgramCompetencyById(Integer id) throws Exception {
        if(null == id)
            throw new InvalidValue("The id is not valid, it can not be null");
        OptionalWrapper<ProgramCompetency> response = repository.getCompetencyById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramCompetency updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency) throws Exception {
        if(null==id)
            throw new InvalidValue("The id is not valid, it can not be null");
        if(null== newProgramCompetency)
            throw new InvalidValue("Instance of competency is invalid, it can not be null");

        OptionalWrapper<ProgramCompetency> response = repository.updateProgramCompetency(id, newProgramCompetency);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramCompetency remove(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("The id is not valid, it can not be null");
        OptionalWrapper<ProgramCompetency> response = repository.remove(id);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public List<ProgramOutcome> listAllProgramOutcomes() {
        return repository.getProgramOutcome();
    }

    @Override
    public ProgramOutcome getProgramOutcomeById(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("The id is not valid, it can not be null");
        OptionalWrapper<ProgramOutcome> response = repository.getProgramOutcomeById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public ProgramOutcome updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome) throws Exception {
        if(null==id)
            throw new InvalidValue("The id is not valid, it can not be null");
        if(null== newProgramOutcome)
            throw new InvalidValue("Instance of outcome is invalid, it can not be null");
        OptionalWrapper<ProgramOutcome> response = repository.updateProgramOutcome(id, newProgramOutcome);
        return response.getValue()
                .orElseThrow(response::getException);
    }

}
