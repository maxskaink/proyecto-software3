import { CriterionDTO } from "./CirterionDTO";
import { OutCome } from "./OutComeDTO";

export interface RubricDTO{
    id: number,
    description: string,
    subjectOutcome: OutCome,
    criteria: CriterionDTO[]

}