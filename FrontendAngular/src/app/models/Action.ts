export interface Action {
    type: string;
    value: string;
    params?: { [key: string]: string | number };
    queryParams?: { [key: string]: string | number };
  }