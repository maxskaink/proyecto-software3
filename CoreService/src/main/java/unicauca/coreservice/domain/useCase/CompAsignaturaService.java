package unicauca.coreservice.domain.useCase;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import unicauca.coreservice.application.in.CompAsignaturaUsesCase;
import unicauca.coreservice.application.out.AsignacionCompAsignaturaRepositoryOut;
import unicauca.coreservice.application.out.CompAsignaturaRepositoryOut;
import unicauca.coreservice.application.out.RaAsignaturaRepositoryOut;
import unicauca.coreservice.domain.exception.InvalidValue;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.PeriodoRepository;

import java.util.List;

@AllArgsConstructor
public class CompAsignaturaService implements CompAsignaturaUsesCase {

    private final CompAsignaturaRepositoryOut repositoryComp;
    private final AsignacionCompAsignaturaRepositoryOut repositoryAsignacion;
    private final PeriodoRepository repositoryPeriodo;
    private final RaAsignaturaRepositoryOut repositoryRaAsignatura;

    @Override
    @Transactional
    public CompetenciaAsignatura addCompetencia(CompetenciaAsignatura newCompetenciaAsignatura, RAAsignatura firstRA, Integer idAsignatura) throws Exception {
        //TODO Mirar como hacer para que valide que ya tenga una RA
        //Validate some information
        if(null==newCompetenciaAsignatura || null == idAsignatura)
            throw new InvalidValue("Instance de Competencia es invalida, no puede ser nula");
        if(null==firstRA)
            throw new InvalidValue("Instance de RA es invalida, no puede ser nula");

        Periodo acutalPeriodo = repositoryPeriodo.getActualPeriodo().getValue()
                .orElseThrow(()->new RuntimeException("Actual Period doesnt exist"));

        //MAchetazo,perdon :c
        Asignatura asignatura = new Asignatura(idAsignatura, "nombre", "descripcion");

        //Create competencia in the main table
        OptionalWrapper<CompetenciaAsignatura> responseCreate = repositoryComp.addCompetenciaAsignatura(newCompetenciaAsignatura);
        CompetenciaAsignatura competencia = responseCreate.getValue()
                .orElseThrow(responseCreate::getException);
        //Asociate in the actual period with asignatura
        AsignacionCompetenciaAsignatura asignacion = new AsignacionCompetenciaAsignatura(
                null,
                competencia,
                asignatura,
                acutalPeriodo,
                null
        );
        OptionalWrapper<AsignacionCompetenciaAsignatura> responseAsignacion =
                repositoryAsignacion.addAsignacionCompetenciaAsignatura(asignacion);

        CompetenciaAsignatura response = responseAsignacion.getValue()
                .orElseThrow(responseAsignacion::getException).getCompetencia();

        //Add First RA to the competencia
        OptionalWrapper<RAAsignatura> responseAddFirstRA =
                repositoryRaAsignatura.addRAAsignatura(firstRA, response.getId());
        responseAddFirstRA.getValue().orElseThrow(responseAddFirstRA::getException);

        return response;
    }

    @Override
    public List<CompetenciaAsignatura> listCompetenciaAsignatura(Integer idAsignatura) {
        if(null==idAsignatura)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        return repositoryComp.listCompetenciaAsignatura(idAsignatura);
    }

    @Override
    public CompetenciaAsignatura getCompetenciaById(Integer id) {
        if(null==id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        return repositoryComp.getCompetenciaById(id).getValue()
                .orElseThrow(()->new NotFound("Competencia con id " + id + " no existe"));
    }

    @Override
    public CompetenciaAsignatura updateCompetenciaById(Integer id, CompetenciaAsignatura newCompetenciaAsignatura) throws Exception {
        if(null == id)
            throw new InvalidValue("id es invalido, no puede ser nulo");
        if(null == newCompetenciaAsignatura)
            throw new InvalidValue("Instance de Competencia es invalida, no puede ser nula");
        OptionalWrapper<CompetenciaAsignatura> response = repositoryComp.updateCompetenciaById(id, newCompetenciaAsignatura);

        return response.getValue()
                .orElseThrow(response::getException);
    }

    @Transactional
    @Override
    public CompetenciaAsignatura removeCompetenciaAsignatura(Integer id) throws Exception {
        //obtener la asignacion en el periodo actual, la competencia y las ra de asignaturas asociadas

        OptionalWrapper<AsignacionCompetenciaAsignatura> asignacionWrapper =
                repositoryAsignacion.findByIdCompetencia(id);

        AsignacionCompetenciaAsignatura asignacion= asignacionWrapper.getValue()
                .orElseThrow(asignacionWrapper::getException);

        asignacion.getRAAsignaturas().forEach(ra -> {
            repositoryRaAsignatura.removeRAAsignatura(ra.getId());
        });
        repositoryComp.removeCompetenciaAsignatura(id);

        OptionalWrapper<AsignacionCompetenciaAsignatura> response =
                repositoryAsignacion.deleteById(asignacion.getId());

        return response.getValue().orElseThrow(response::getException).getCompetencia();
    }

}
