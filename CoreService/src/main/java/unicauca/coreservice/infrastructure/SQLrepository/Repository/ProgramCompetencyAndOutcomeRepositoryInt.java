package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACompetenciaProgramaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPARaProgramaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CompProgramaMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RAProgramaMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProgramCompetencyAndOutcomeRepositoryInt implements ProgramCompetencyAndOutcomeRepositoryOutInt {

    private final JPACompetenciaProgramaRepository JPAcompPrograma;
    private final JPARaProgramaRepository JPARaPrograma;

    @Override
    public OptionalWrapper<ProgramCompetency> add(ProgramCompetency newProgramCompetency) {

        try{
            newProgramCompetency.setId(null);
            ProgramCompetencyEntity compProg = CompProgramaMapper.toCompProgramaEntity(newProgramCompetency);
            compProg.getLearningOutcomes().setCompetency(compProg);

            ProgramCompetencyEntity response = this.JPAcompPrograma.save(compProg);
            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(response));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<ProgramCompetency> listAll() {
        List<ProgramCompetency> response = new ArrayList<>();
        for(ProgramCompetencyEntity comp : this.JPAcompPrograma.findAllByActivadoTrue())
            response.add(CompProgramaMapper.toCompPrograma(comp));
        return response;
    }

    @Override
    public OptionalWrapper<ProgramCompetency> getCompetencyById(Integer id) {
        try{
            ProgramCompetencyEntity response = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(()-> new NotFound("Competencia con id " + id + " no se encuentra") );

            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(response));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<ProgramCompetency> updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency) {
        try{
            ProgramCompetencyEntity response = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("Competencia con id " + id + " no se encuentra"));
            ProgramCompetencyEntity newComp = CompProgramaMapper.toCompProgramaEntity(newProgramCompetency);
            newComp.setId(response.getId());
            newComp.setLearningOutcomes(response.getLearningOutcomes());

            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(this.JPAcompPrograma.save(newComp)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }
    // TODO
    // REVIEW IF THIS REMOVE REMOVE BOTH COMPETENCY AND OUTCOME
    @Override
    public OptionalWrapper<ProgramCompetency> remove(Integer id) {
        try{
            ProgramCompetencyEntity actualComp = this.JPAcompPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("Competencia con id " + id + " no se encuentra"));
            actualComp.setIsActivated(false);
            actualComp.setDescription(actualComp.getDescription() + " (Eliminada)" + System.nanoTime());
            return new OptionalWrapper<>(CompProgramaMapper.toCompPrograma(this.JPAcompPrograma.save(actualComp)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<ProgramOutcome> getProgramOutcome() {

        List<ProgramOutcome> response = new ArrayList<>();
        for(ProgramOutcomeEntity ra : this.JPARaPrograma.findAllByActivadoTrue())
            response.add(RAProgramaMapper.toRAPrograma(ra));
        return response;
    }

    @Override
    public OptionalWrapper<ProgramOutcome> getProgramOutcomeById(Integer id) {
        try{
            ProgramOutcomeEntity response = this.JPARaPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RA con id " + id + " no se encuentra"));
            return new OptionalWrapper<>(RAProgramaMapper.toRAPrograma(response));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<ProgramOutcome> updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome) {
        try{
            ProgramOutcomeEntity response = this.JPARaPrograma.findByIdAndActivadoTrue(id)
                    .orElseThrow(() -> new NotFound("RA con id " + id + " no se encuentra"));
            ProgramOutcomeEntity newRa = RAProgramaMapper.toRAProgramaEntity(newProgramOutcome);

            newRa.setId(response.getId());
            newRa.setCompetency(response.getCompetency());

            return new OptionalWrapper<>(RAProgramaMapper.toRAPrograma(this.JPARaPrograma.save(newRa)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
