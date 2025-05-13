package unicauca.coreservice.domain.useCase;

import unicauca.coreservice.application.in.ProgramCompetenceAndOutcomeInt;
import unicauca.coreservice.application.out.CompAndRAProgramaRepositoryOut;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAPrograma;

import java.util.List;

public class ProgramCompetenceAndRAProgramaService implements ProgramCompetenceAndOutcomeInt {

    private final CompAndRAProgramaRepositoryOut repository;

    public ProgramCompetenceAndRAProgramaService(CompAndRAProgramaRepositoryOut repository) {
        this.repository = repository;
    }

    @Override
    public CompetenciaPrograma addProgramCompetence(CompetenciaPrograma newCompetenciaPrograma) throws Exception {
        OptionalWrapper<CompetenciaPrograma> response = repository.addCompPrograma(newCompetenciaPrograma);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public List<CompetenciaPrograma> getCompetenciaProgramas() {
        return repository.getCompetenciaProgramas();
    }

    @Override
    public CompetenciaPrograma getCompById(Integer id) throws Exception {
        if(null == id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<CompetenciaPrograma> response = repository.getCompById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public CompetenciaPrograma updateCompPrograma(Integer id, CompetenciaPrograma newCompetenciaPrograma) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null==newCompetenciaPrograma)
            throw new InvalidValue("Instance de competencia es invalida, no puede ser nula");

        OptionalWrapper<CompetenciaPrograma> response = repository.updateCompPrograma(id, newCompetenciaPrograma);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public CompetenciaPrograma deleteCompPrograma(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<CompetenciaPrograma> response = repository.deleteCompPrograma(id);

        CompetenciaPrograma compDeleted = response.getValue()
                .orElseThrow(response::getException);

        repository.deleteRAPrograma(compDeleted.getRaPrograma().getId());

        return compDeleted;
    }

    @Override
    public List<RAPrograma> getRAProgramas() {
        return repository.getRAPrograma();
    }

    @Override
    public RAPrograma getRAById(Integer id) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        OptionalWrapper<RAPrograma> response = repository.getRAById(id);
        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Override
    public RAPrograma updateRAPrograma(Integer id, RAPrograma newRAPrograma) throws Exception {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null==newRAPrograma)
            throw new InvalidValue("Instance de RA es invalida, no puede ser nula");
        OptionalWrapper<RAPrograma> response = repository.updateRAPrograma(id, newRAPrograma);
        return response.getValue()
                .orElseThrow(response::getException);
    }

}
