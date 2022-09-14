package org.bsf.transfers.controllers

import org.bsf.transfers.common.Account
import org.bsf.transfers.domain.AccountInactive
import org.bsf.transfers.domain.AccountView
import org.bsf.transfers.services.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncWebRequest

@RestController
@RequestMapping("/api/v2/accounts")
class AccountInfoController(
    private val accountService: AccountService
) {

    @GetMapping("/info/{iban}", produces = [APPLICATION_JSON_VALUE])
    fun accountInfo(@PathVariable(name = "iban") iban: String): ResponseEntity<Account> =
        ResponseEntity(accountService.inquireBy(iban), HttpStatus.OK)


    @PostMapping("/create", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createAccount(@RequestBody account: AccountView): ResponseEntity<Account> {
        return ResponseEntity(accountService.create(account), HttpStatus.CREATED)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(exception: Exception, status: HttpStatus): ResponseEntity<ApiError> {
        return ResponseEntity<ApiError>(ApiError(status, exception), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccountInactive::class)
    fun handleAccountInactive(exception: Exception, httpStatus: HttpStatus): ResponseEntity<ApiError> {
        return ResponseEntity<ApiError>(ApiError(httpStatus, exception), HttpStatus.CONFLICT)
    }
}