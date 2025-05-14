package unicauca.coreservice.domain.useCase;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.out.AssignCompetencyToSubjectRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepositoryInt;

import java.util.List;

@AllArgsConstructor
public class CompAsignaturaService implements SubjectCompetencyInt {

    private final SubjectCompetencyRepositoryOutInt repositoryComp;
    private final AssignCompetencyToSubjectRepositoryOutInt repositoryAsignacion;
    private final TermRepositoryInt repositoryPeriodo;
    private final SubjectOutcomeRepositoryOutInt repositoryRaAsignatura;

    @Override
    @Transactional
    public SubjectCompetency addCompetency(SubjectCompetency newCompetenciaAsignatura, SubjectOutcome initialOutcome, Integer idAsignatura) throws Exception {
        //TODO Mirar como hacer para que valide que ya tenga una RA
        //Validate some information
        if(null==newCompetenciaAsignatura || null == idAsignatura)
            throw new InvalidValue("Instance de Competencia es invalida, no puede ser nula");
        if(null== initialOutcome)
            throw new InvalidValue("Instance de RA es invalida, no puede ser nula");

        Term acutalTerm = repositoryPeriodo.getActualPeriodo().getValue()
                .orElseThrow(()->new RuntimeException("Actual Period doesnt exist"));

        //MAchetazo,perdon :c
        Subject subject = new Subject(idAsignatura, "nombre", "descripcion");

        //Create competencia in the main table
        OptionalWrapper<SubjectCompetency> responseCreate = repositoryComp.add(newCompetenciaAsignatura);
        SubjectCompetency competencia = responseCreate.getValue()
                .orElseThrow(responseCreate::getException);
        //Asociate in the actual period with subject
        AssignCompetencyToSubject asignacion = new AssignCompetencyToSubject(
                null,
                competencia,
                subject,
                acutalTerm,
                null
        );
        OptionalWrapper<AssignCompetencyToSubject> responseAsignacion =
                repositoryAsignacion.add(asignacion);

        SubjectCompetency response = responseAsignacion.getValue()
                .orElseThrow(responseAsignacion::getException).getCompetency();

        //Add First RA to the competencia
        OptionalWrapper<SubjectOutcome> responseAddFirstRA =
                repositoryRaAsignatura.add(initialOutcome, response.getId());
        responseAddFirstRA.getValue().orElseThrow(responseAddFirstRA::getException);

        return response;
    }

    @Override
    public List<SubjectCompetency> listSubjectCompetencies(Integer idAsignatura) {
        if(null==idAsignatura)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        return repositoryComp.listAllBySubjectId(idAsignatura);
    }

    @Override
    public SubjectCompetency getCompetencyById(Integer id) {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        return repositoryComp.getById(id).getValue()
                .orElseThrow(()->new NotFound("Competencia con id " + id + " no existe"));
    }

    @Override
    public SubjectCompetency updateCompetencyById(Integer id, SubjectCompetency newSubjectCompetency) throws Exception {
        if(null == id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null == newSubjectCompetency)
            throw new InvalidValue("Instance de Competencia es invalida, no puede ser nula");
        OptionalWrapper<SubjectCompetency> response = repositoryComp.update(id, newSubjectCompetency);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Transactional
    @Override
    public SubjectCompetency removeSubjectCompetency(Integer id) throws Exception {
        //obtener la asignacion en el periodo actual, la competencia y las ra de asignaturas asociadas

        OptionalWrapper<AssignCompetencyToSubject> asignacionWrapper =
                repositoryAsignacion.getBySubjectId(id);

        AssignCompetencyToSubject asignacion= asignacionWrapper.getValue()
                .orElseThrow(asignacionWrapper::getException);

        asignacion.getSubjectOutcomes().forEach(ra -> {
            repositoryRaAsignatura.remove(ra.getId());
        });
        repositoryComp.remove(id);

        OptionalWrapper<AssignCompetencyToSubject> response =
                repositoryAsignacion.remove(asignacion.getId());

        return response.getValue().orElseThrow(response::getException).getCompetency();
    }

}
