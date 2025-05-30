import { CriterionDTO } from "./CirterionDTO";
import { SubjectOutcome } from "./SubjectOutcomeDTO";

export interface RubricDTO{
    id: number,
    description: string,
    subjectOutcome: SubjectOutcome,
    criteria: CriterionDTO[]

}