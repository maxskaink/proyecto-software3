import { LevelDTO } from "./LevelDTO";
import { RubricDTO } from "./RubricDTO";

export interface CriterionDTO{
    id: number
    weight: number,
    name: string,
    rubric: RubricDTO,
    levels: LevelDTO[]
}