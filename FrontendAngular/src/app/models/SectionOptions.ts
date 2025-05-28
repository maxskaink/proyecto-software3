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
  
 export interface Action {
    type: 'navigate' | 'modal' | 'scroll';
    value: string;
  }