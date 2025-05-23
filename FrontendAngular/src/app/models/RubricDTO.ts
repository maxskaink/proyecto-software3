import { CriterionDTO } from "./CirterionDTO";
import { OutCome } from "./SubjectOutcomeDTO";

export interface RubricDTO{
    id: number,
    description: string,
    subjectOutcome: OutCome,
    criteria: CriterionDTO[]

}