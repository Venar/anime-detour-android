/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database.event;

import com.animedetour.android.model.Event;
import com.animedetour.android.model.MetaData;
import com.animedetour.android.model.transformer.Transformer;
import com.animedetour.api.sched.ScheduleEndpoint;
import com.animedetour.api.sched.model.ApiEvent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import monolog.Monolog;

import java.sql.SQLException;
import java.util.List;

/**
 * Looks up a list of all events locally after updating with the API.
 *
 * Events will be ordered by start time.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class AllEventsWorker extends SyncEventsWorker
{
    final Dao<Event, String> localAccess;

    public AllEventsWorker(
        Dao<Event, String> localAccess,
        ScheduleEndpoint remoteAccess,
        Dao<MetaData, Integer> metaData,
        Monolog logger,
        Transformer<ApiEvent, Event> eventTransformer
    ) {
        super(localAccess, metaData, remoteAccess, eventTransformer, logger);

        this.localAccess = localAccess;
    }

    @Override
    public List<Event> lookupLocal() throws SQLException
    {
        QueryBuilder<Event, String> builder = this.localAccess.queryBuilder();
        builder.orderBy("start", true);
        builder.orderBy("name", true);

        PreparedQuery<Event> query = builder.prepare();
        List<Event> result = this.localAccess.query(query);

        return result;
    }
}
