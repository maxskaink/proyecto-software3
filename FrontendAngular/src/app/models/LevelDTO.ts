import { CriterionDTO } from "./CirterionDTO";

export interface LevelDTO{
    id: number,
    category: string;
    description: string;
    percentage: number;
    criterion: CriterionDTO;
}