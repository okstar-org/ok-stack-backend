/*
 * * Copyright (c) 2022 船山信息 chuanshaninfo.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.auth.service;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.auth.domain.AuthSession;
import org.okstar.platform.auth.repository.AuthSessionRepository;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;

import java.util.List;

@Transactional
@ApplicationScoped
public class AuthSessionServiceImpl implements AuthSessionService {

    @Inject
    AuthSessionRepository repository;


    @Override
    public void save(AuthSession authSession) {
        repository.persist(authSession);
    }

    @Override
    public List<AuthSession> findAll() {
        return repository.findAll().stream().toList();
    }

    @Override
    public OkPageResult<AuthSession> findPage(OkPageable page) {
        var all = repository.findAll();
        var query = all.page(Page.of(page.getPageIndex(), page.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public AuthSession get(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(AuthSession authSession) {
        repository.delete(authSession);
    }

    @Override
    public AuthSession get(String uuid) {
        return repository.find("uuid", uuid).firstResult();
    }
}
