package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import com.meli.fede.markoo.proxy.manager.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.manager.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final MongoRepository mongoRepository;

    private List<RequestData> getData() {
        return this.mongoRepository.getData();
    }

    private static Comparator<BaseInfoResponse> getComparing() {
        return Comparator.comparingLong(BaseInfoResponse::getDeniedCant).thenComparingLong(BaseInfoResponse::getRequestCant);
    }

    public List<IpInfoResponse> getCounterByIp() {
        final List<RequestData> data = this.getData();
        final ArrayList<IpInfoResponse> collect = data.stream()
                .collect(ArrayList::new
                        , (a, d) -> {
                            final Optional<IpInfoResponse> optional = a.stream()
                                    .filter(iir -> Objects.equals(iir.getIp(), d.getIp())).findFirst();
                            if (optional.isPresent()) {
                                final IpInfoResponse info = optional.get();
                                info.setDeniedCant(d.getDeniedCant() + info.getDeniedCant());
                                info.setRequestCant(d.getRequestedCant() + info.getRequestCant());
                            } else {
                                a.add(IpInfoResponse.from(d));
                            }
                        }
                        , ArrayList::addAll);
        collect.sort(getComparing());
        return collect;
    }

    public List<ComboInfoResponse> getCounterByCombo() {
        final List<RequestData> data = this.getData();
        final ArrayList<ComboInfoResponse> collect = data.stream()
                .collect(ArrayList::new
                        , (a, d) -> {
                            final Optional<ComboInfoResponse> optional = a.stream()
                                    .filter(cir -> Objects.equals(cir.getCombo(), d.getCombo())).findFirst();
                            if (optional.isPresent()) {
                                final ComboInfoResponse info = optional.get();
                                info.setDeniedCant(d.getDeniedCant() + info.getDeniedCant());
                                info.setRequestCant(d.getRequestedCant() + info.getRequestCant());
                            } else {
                                a.add(ComboInfoResponse.from(d));
                            }
                        }
                        , ArrayList::addAll);
        collect.sort(getComparing());
        return collect;
    }

    public List<PathInfoResponse> getCounterByPath() {
        final List<RequestData> data = this.getData();
        final ArrayList<PathInfoResponse> collect = data.stream()
                .collect(ArrayList::new
                        , (a, d) -> {
                            final Optional<PathInfoResponse> optional = a.stream()
                                    .filter(iir -> Objects.equals(iir.getPath(), d.getPath())).findFirst();
                            if (optional.isPresent()) {
                                final PathInfoResponse info = optional.get();
                                info.setDeniedCant(d.getDeniedCant() + info.getDeniedCant());
                                info.setRequestCant(d.getRequestedCant() + info.getRequestCant());
                            } else {
                                a.add(PathInfoResponse.from(d));
                            }
                        }
                        , ArrayList::addAll);
        collect.sort(getComparing());
        return collect;
    }

    public List<UserAgentInfoResponse> getCounterByUserAgent() {
        final List<RequestData> data = this.getData();
        final ArrayList<UserAgentInfoResponse> collect = data.stream()
                .collect(ArrayList::new
                        , (a, d) -> {
                            final Optional<UserAgentInfoResponse> optional = a.stream()
                                    .filter(iir -> Objects.equals(iir.getUserAgent(), d.getUserAgent())).findFirst();
                            if (optional.isPresent()) {
                                final UserAgentInfoResponse info = optional.get();
                                info.setDeniedCant(d.getDeniedCant() + info.getDeniedCant());
                                info.setRequestCant(d.getRequestedCant() + info.getRequestCant());
                            } else {
                                a.add(UserAgentInfoResponse.from(d));
                            }
                        }
                        , ArrayList::addAll);
        collect.sort(getComparing());
        return collect;
    }
}
