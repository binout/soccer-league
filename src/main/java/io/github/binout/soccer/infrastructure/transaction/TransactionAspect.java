/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.soccer.infrastructure.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionAspect {

    private final TransactionManager transactionManager;

    public TransactionAspect(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(javax.transaction.Transactional)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            this.transactionManager.doBegin();
            Object proceed = joinPoint.proceed();
            this.transactionManager.doCommit();
            return proceed;
        } catch (RuntimeException e) {
            this.transactionManager.doRollback();
            throw e;
        }
    }
}