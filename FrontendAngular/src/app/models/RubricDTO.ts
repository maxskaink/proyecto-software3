import { CriterionDTO } from "./CirterionDTO";
import { SubjectOutcome } from "./SubjectOutcome";

export interface RubricDTO{
    id: number,
    description: string,
    subjectOutcome: SubjectOutcome,
    criteria: CriterionDTO[]

}