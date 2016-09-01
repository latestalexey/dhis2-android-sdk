package org.hisp.dhis.client.sdk.rules;

import org.apache.commons.lang3.math.NumberUtils;
import org.hisp.dhis.commons.util.ExpressionFunctions;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by markusbekken on 20.05.2016.
 */
abstract class DhisFunction {

    private static List<DhisFunction> dhisFunctions = Arrays.asList(
            new DhisFunction("d2:daysBetween", 2) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    return String.valueOf(getIntervalsBetween(1, parameters, expression));
                }
            },
            new DhisFunction("d2:weeksBetween", 2) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    return String.valueOf(getIntervalsBetween(7, parameters, expression));
                }
            },
            new DhisFunction("d2:monthsBetween", 2) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            },
            new DhisFunction("d2:yearsBetween", 2) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            },
            new DhisFunction("d2:floor", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    Integer floored = (int) NumberUtils.toDouble(parameters.get(0));
                    return floored.toString();
                }
            }
            ,
            new DhisFunction("d2:modulus", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:concatenate", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:addDays", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:zing", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:oizp", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:count", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:countIfZeroPos", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:countIfValue", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:ceil", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:round", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:hasValue", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    String variableName = parameters.get(0).replace("'","");
                    ProgramRuleVariableValue variable = valueMap.getProgramRuleVariableValue(variableName);
                    if(variable != null) {
                        return variable.hasValue() ? "true" : "false";
                    }
                    else {
                        return "false";
                    }
                }
            }
            ,
            new DhisFunction("d2:lastEventDate", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:validatePattern", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:addControlDigits", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:checkControlDigits", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:left", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:right", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:substring", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:split", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
            ,
            new DhisFunction("d2:length", 1) {
                @Override
                public String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression) {
                    throw new NotImplementedException();
                }
            }
    );


    private static int getIntervalsBetween(int daysInInterval, List<String> parameters, String expression) {
        DateTimeFormatter f =  DateTimeFormatter.ofPattern(RuleEngineVariableValueMap.DATE_PATTERN);

        try {
            return (ExpressionFunctions.daysBetween(parameters.get(0),parameters.get(1)) / daysInInterval);
        }
        catch (ParseException e) {
            //TODO: Log the error and the expression
            return 0;
        }
    }

    public static List<DhisFunction> getDhisFunctions() {
        return dhisFunctions;
    }

    private String name;
    private Integer parameters;

    public DhisFunction(String name, Integer parameters){
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Integer getParameters() {
        return parameters;
    }

    public abstract String execute(List<String> parameters, RuleEngineVariableValueMap valueMap, String expression);
}