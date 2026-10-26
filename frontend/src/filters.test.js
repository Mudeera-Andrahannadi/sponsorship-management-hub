import { describe, expect, test } from 'vitest';
import { match, tally } from './filters.js';

const KEYS = ['Available', 'In Progress', 'Locked'];

const LIST = [
    { name: 'Hemas', field: 'Consumer goods', contact: null, status: 'Available' },
    { name: 'Dialog', field: 'Telecom', contact: 'Ruwan', status: 'In Progress' },
    { name: 'SLT', field: 'Telecom', contact: undefined, status: 'Locked' },
    { name: 'Virtusa', field: 'Software', contact: 'Anika', status: 'Available' }
];

describe('tally', () =>
{
    test('counts each status and the total', () =>
    {
        expect(tally(LIST, KEYS)).toEqual({ all: 4, Available: 2, 'In Progress': 1, Locked: 1 });
    });

    test('gives zeros for an empty list', () =>
    {
        expect(tally([], KEYS)).toEqual({ all: 0, Available: 0, 'In Progress': 0, Locked: 0 });
    });

    test('ignores a status it does not know', () =>
    {
        const odd = [{ name: 'X', status: 'Mystery' }];
        expect(tally(odd, KEYS).Mystery).toBeUndefined();
    });
});

describe('match', () =>
{
    const fields = ['name', 'field', 'contact'];

    test('filters by status', () =>
    {
        const names = LIST.filter(match('', 'Locked', fields)).map((item) => item.name);
        expect(names).toEqual(['SLT']);
    });

    test('searches any field, ignoring case and spaces around the term', () =>
    {
        const names = LIST.filter(match('  TELECOM ', 'all', fields)).map((item) => item.name);
        expect(names).toEqual(['Dialog', 'SLT']);
    });

    test('survives null and missing fields', () =>
    {
        expect(LIST.filter(match('zzz', 'all', fields))).toEqual([]);
    });

    test('combines the status and the search', () =>
    {
        const names = LIST.filter(match('telecom', 'Available', fields));
        expect(names).toEqual([]);
    });
});
