import { LevelEntity } from "./LevelEntity";

export interface CriterionEntity{
    weight: number,
    name: string,
    levels:LevelEntity[]
}