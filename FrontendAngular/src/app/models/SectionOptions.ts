import { Action } from "./Action";

export interface SectionOption {
    title: string;
    description: string;
    showButtonOne: boolean;
    buttonTextOne?: string;
    actionOne?: Action;
    showButtonTwo: boolean;
    buttonTextTwo?: string;
    actionTwo?: Action;
  }
