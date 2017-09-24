package io.github.binout.soccer.infrastructure.transaction;

public interface TransactionManager<T> {

    T doGetTransaction();

    void doBegin();

    void doCommit();

    void doRollback();
}
