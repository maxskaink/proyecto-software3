package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.CompAndRAProgramaRepositoryOut;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.RAPrograma;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACompetenciaProgramaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPARaProgramaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAProgramaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CompProgramaMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RAProgramaMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CompAndRaProgramaRepository implements CompAndRAProgramaRepositoryOut {

    private final JPACompetenciaProgramaRepository JPAcompPrograma;
    private final JPARaProgramaRepository JPARaPrograma;

    @Override
    public OptionalWrapper<CompetenciaPrograma> addCompPrograma(CompetenciaPrograma newCompetenciaPrograma) {

        try{
            newCompetenciaPrograma.setId(null);
            CompetenciaProgramaEntity compProg = CompProgramaMapper.toCompProgramaEntity(newCompetenciaPrograma);
            compProg.getResultadosAprendizaje().setCompetencia(compProg);

            CompetenciaProgramaEntity response = this.JPAcompPrograma.save(compProg);
            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(response));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<CompetenciaPrograma> getCompetenciaProgramas() {
        List<CompetenciaPrograma> response = new ArrayList<>();
        for(CompetenciaProgramaEntity comp : this.JPAcompPrograma.findAllByActivadoTrue())
            response.add(CompProgramaMapper.toCompPrograma(comp));
        return response;
    }

    @Override
    public OptionalWrapper<CompetenciaPrograma> getCompById(Integer id) {
        try{
            CompetenciaProgramaEntity response = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia con id " + id + " no se encuentra") );

            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(response));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetenciaPrograma> updateCompPrograma(Integer id, CompetenciaPrograma newCompetenciaPrograma) {
        try{
            CompetenciaProgramaEntity response = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("Competencia con id " + id + " no se encuentra"));
            CompetenciaProgramaEntity newComp = CompProgramaMapper.toCompProgramaEntity(newCompetenciaPrograma);
            newComp.setId(response.getId());
            newComp.setResultadosAprendizaje(response.getResultadosAprendizaje());

            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(this.JPAcompPrograma.save(newComp)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<CompetenciaPrograma> deleteCompPrograma(Integer id) {
        try{
            CompetenciaProgramaEntity actualComp = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("Competencia con id " + id + " no se encuentra"));
            actualComp.setActivado(false);
            actualComp.setDescripcion(actualComp.getDescripcion() + " (Eliminada)" + System.nanoTime());
            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(this.JPAcompPrograma.save(actualComp)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<RAPrograma> getRAPrograma() {

        List<RAPrograma> response = new ArrayList<>();
        for(RAProgramaEntity ra : this.JPARaPrograma.findAllByActivadoTrue())
            response.add(RAProgramaMapper.toRAPrograma(ra));
        return response;
    }

    @Override
    public OptionalWrapper<RAPrograma> getRAById(Integer id) {
        try{
            RAProgramaEntity response = this.JPARaPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RA con id " + id + " no se encuentra"));
            return new OptionalWrapper<>(RAProgramaMapper.toRAPrograma(response));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<RAPrograma> updateRAPrograma(Integer id, RAPrograma newRAPrograma) {
        try{
            RAProgramaEntity response = this.JPARaPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RA con id " + id + " no se encuentra"));
            RAProgramaEntity newRa = RAProgramaMapper.toRAProgramaEntity(newRAPrograma);

            newRa.setId(response.getId());
            newRa.setCompetencia(response.getCompetencia());

            return new OptionalWrapper<>(RAProgramaMapper.toRAPrograma(this.JPARaPrograma.save(newRa)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<RAPrograma> deleteRAPrograma(Integer id) {
        try{
            RAProgramaEntity response = this.JPARaPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RA con id " + id + " no se encuentra"));
            response.setActivado(false);
            return new OptionalWrapper<>(RAProgramaMapper.toRAPrograma(this.JPARaPrograma.save(response)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
}
