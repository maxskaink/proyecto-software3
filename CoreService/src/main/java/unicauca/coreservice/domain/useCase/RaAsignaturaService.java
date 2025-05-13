package unicauca.coreservice.domain.useCase;

import unicauca.coreservice.application.in.RaAsignaturaUsesCase;
import unicauca.coreservice.domain.model.RAAsignatura;

import java.util.List;

public class RaAsignaturaService implements RaAsignaturaUsesCase {
    @Override
    public RAAsignatura addRAAsignatura(RAAsignatura newRAAsignatura, Integer idCompetencia, Integer idAsignatura) {
        return null;
    }

    @Override
    public List<RAAsignatura> listRAAsignatura(Integer idAsignatura) {
        return List.of();
    }

    @Override
    public List<RAAsignatura> listRAAsignaturaActualPeriod(Integer idAsignatura) {
        return List.of();
    }

    @Override
    public List<RAAsignatura> listRAAsignaturaByCompetencia(Integer idCompetencia) {
        return List.of();
    }

    @Override
    public RAAsignatura getByIdRAAsignatura(Integer id) {
        return null;
    }

    @Override
    public RAAsignatura updateByIdRAAsignatura(Integer id, RAAsignatura newRAAsignatura) {
        return null;
    }

    @Override
    public RAAsignatura removeRAAsignatura(Integer id) {
        return null;
    }

    @Override
    public RAAsignatura copyRAAsignatura(Integer idRAAOriginal, Integer idCompetencia, Integer idAsignatura) {
        return null;
    }
}
