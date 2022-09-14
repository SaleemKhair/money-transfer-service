package org.bsf.transfers.controllers

import org.bsf.transfers.domain.InvalidTransfer
import org.bsf.transfers.domain.MoneyTransferView
import org.bsf.transfers.services.MoneyTransfersService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/transfers")
class TransferController(
    private val moneyTransfersService: MoneyTransfersService
) {
    @PostMapping("/transfer", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun transferMoney(@RequestBody moneyTransferView: MoneyTransferView): ResponseEntity<MoneyTransferView> =
        ResponseEntity(moneyTransfersService.transferMoney(moneyTransferView), HttpStatus.ACCEPTED)


    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(exception: Exception, httpStatus: HttpStatus): ResponseEntity<ApiError> {
        return ResponseEntity<ApiError>(ApiError(httpStatus, exception), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidTransfer::class)
    fun handleInvalidTransfer(exception: Exception, httpStatus: HttpStatus): ResponseEntity<ApiError> {
        return ResponseEntity<ApiError>(ApiError(httpStatus, exception), HttpStatus.NOT_ACCEPTABLE)
    }
}