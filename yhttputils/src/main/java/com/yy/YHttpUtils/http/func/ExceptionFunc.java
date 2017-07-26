package com.yy.YHttpUtils.http.func;


import com.yy.YHttpUtils.exception.FactoryException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by ly on 17-7-26.
 */

public class ExceptionFunc implements Function<Throwable,ObservableSource> {
    @Override
    public ObservableSource apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(FactoryException.analysisExcetpion(throwable));
    }
}
