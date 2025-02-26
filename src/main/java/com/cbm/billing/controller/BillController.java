package com.cbm.billing.controller;


import com.cbm.billing.dto.create.CreateBillDTO;
import com.cbm.billing.dto.create.CreateBillResponse;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.service.IBillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/bills")
@CrossOrigin("*")
public class BillController {
    private final IBillService billService;

    public BillController(IBillService billService) {
        this.billService = billService;
    }

    /**
     * Create a new bill with the given {@link CreateBillDTO}.
     * @param createBillDTO the data for the new bill
     * @return a {@link ResponseEntity} containing the created bill, or an error response if the bill could not be
     *     created
     * @throws AccountNotFoundException if the account with the given ID does not exist
     */
    @PostMapping("/create")
    public ResponseEntity<CreateBillResponse> createBill(@RequestBody @Valid CreateBillDTO createBillDTO) throws AccountNotFoundException {
        CreateBillResponse createBillResponse = billService.createBill(createBillDTO);
        return ResponseEntity.ok(createBillResponse);
    }
}
