package com.example.bankappapplication.mapper;

import com.example.bankappapplication.dto.OperationDTO;
import com.example.bankappapplication.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {

    public OperationDTO mapToOperationDTO(Operation operation) {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setId(operation.getId());
        operationDTO.setOperationDate(operation.getOperationDate());
        operationDTO.setType(operation.getType());
        operationDTO.setAmount(operation.getAmount());
        operationDTO.setAccountId(operation.getAccount().getId());
        return operationDTO;
    }

    public Operation mapToOperation(OperationDTO operationDTO) {
        Operation operation = new Operation();
        operation.setId(operationDTO.getId());
        operation.setOperationDate(operationDTO.getOperationDate());
        operation.setType(operationDTO.getType());
        operation.setAmount(operationDTO.getAmount());
        return operation;
    }
}