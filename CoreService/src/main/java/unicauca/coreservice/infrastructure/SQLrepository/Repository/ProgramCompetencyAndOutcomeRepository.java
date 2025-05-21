package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.exception.DuplicateInformation;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAProgramCompetencyRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAProgramOutcomeRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.ProgramCompetencyMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.ProgramOutcomeMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProgramCompetencyAndOutcomeRepository implements ProgramCompetencyAndOutcomeRepositoryOutInt {

    private final JPAProgramCompetencyRepository programCompetencyRepository;
    private final JPAProgramOutcomeRepository programOutcomeRepository;

    @Override
    public OptionalWrapper<ProgramCompetency> add(ProgramCompetency newProgramCompetency) {

        try{
            newProgramCompetency.setId(null);
            newProgramCompetency.getProgramOutcome().setId(null);
            ProgramCompetencyEntity compProg = ProgramCompetencyMapper.toProgramCompetencyEntity(newProgramCompetency);
            compProg.getLearningOutcomes().setCompetency(compProg);

            ProgramCompetencyEntity response = this.programCompetencyRepository.save(compProg);
            return new OptionalWrapper<>(ProgramCompetencyMapper.toProgramCompetency(response));
        }catch(DataIntegrityViolationException e){
            return new OptionalWrapper<>(new DuplicateInformation("The Competency " + newProgramCompetency.getDescription() + " allready exist"));
        }
        catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<ProgramCompetency> listAll() {
        List<ProgramCompetency> response = new ArrayList<>();
        for(ProgramCompetencyEntity comp : this.programCompetencyRepository.findByIsActivatedTrue())
            response.add(ProgramCompetencyMapper.toProgramCompetency(comp));
        return response;
    }

    @Override
    public OptionalWrapper<ProgramCompetency> getCompetencyById(Integer id) {
        try{
            ProgramCompetencyEntity response = this.programCompetencyRepository.findByIdAndIsActivatedTrue(id)
                    .orElseThrow(()-> new NotFound("Competency with id " + id + " was not found") );

            return new OptionalWrapper<>(ProgramCompetencyMapper.toProgramCompetency(response));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<ProgramCompetency> updateProgramCompetency(Integer id, ProgramCompetency newProgramCompetency) {
        try{
            ProgramCompetencyEntity response = this.programCompetencyRepository.findByIdAndIsActivatedTrue(id)
                    .orElseThrow(() -> new NotFound("Competency with id " + id + " was not found"));
            ProgramCompetencyEntity newComp = ProgramCompetencyMapper.toProgramCompetencyEntity(newProgramCompetency);
            newComp.setId(response.getId());
            newComp.setLearningOutcomes(response.getLearningOutcomes());

            return new OptionalWrapper<>(ProgramCompetencyMapper.toProgramCompetency(this.programCompetencyRepository.save(newComp)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }
    // TODO
    // REVIEW IF THIS REMOVE REMOVE BOTH COMPETENCY AND OUTCOME
    @Override
    public OptionalWrapper<ProgramCompetency> remove(Integer id) {
        try{
            ProgramCompetencyEntity actualComp = this.programCompetencyRepository.findByIdAndIsActivatedTrue(id)
                    .orElseThrow(() -> new NotFound("Competency with id " + id + " was not found"));
            actualComp.setActivated(false);
            String originalDescription =actualComp.getDescription();
            actualComp.setDescription(originalDescription + " (Removed)" + System.nanoTime());
            this.programCompetencyRepository.save(actualComp);
            actualComp.setDescription(originalDescription);
            return new OptionalWrapper<>(ProgramCompetencyMapper.toProgramCompetency(actualComp));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<ProgramOutcome> getProgramOutcome() {

        List<ProgramOutcome> response = new ArrayList<>();
        for(ProgramOutcomeEntity ra : this.programOutcomeRepository.findByIsActivatedTrue())
            response.add(ProgramOutcomeMapper.toProgramOutcome(ra));
        return response;
    }

    @Override
    public OptionalWrapper<ProgramOutcome> getProgramOutcomeById(Integer id) {
        try{
            ProgramOutcomeEntity response = this.programOutcomeRepository.findActiveProgramOutcomeById(id)
                    .orElseThrow(() -> new NotFound("Outcome with id " + id + " was not found"));
            return new OptionalWrapper<>(ProgramOutcomeMapper.toProgramOutcome(response));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<ProgramOutcome> updateProgramOutcome(Integer id, ProgramOutcome newProgramOutcome) {
        try{
            ProgramOutcomeEntity response = this.programOutcomeRepository.findActiveProgramOutcomeById(id)
                    .orElseThrow(() -> new NotFound("Learning Outcome with id  " + id + " was not found"));
            ProgramOutcomeEntity newRa = ProgramOutcomeMapper.toProgramOutcomeEntity(newProgramOutcome);

            newRa.setId(response.getId());
            newRa.setCompetency(response.getCompetency());

            return new OptionalWrapper<>(ProgramOutcomeMapper.toProgramOutcome(this.programOutcomeRepository.save(newRa)));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
